import { call, put } from 'redux-saga/effects';
import { createFetch } from '@/src/utils/createFetch';
import { setErrorsState, setLoadingState } from '@/src/reduxjs/base';

type SagaHandlingPropsType<T> = {
  href: string;
  server: 'auth' | 'messenger';
  method: 'GET' | 'POST' | 'PATCH' | 'DELETE';
  body?: object;
  action?: (data?: T) => void;
  isDataInAction?: boolean;
};

function* sagaHandling<T>({
  href,
  server,
  method,
  body,
  action,
  isDataInAction,
}: SagaHandlingPropsType<T>) {
  yield put(setLoadingState(true));
  const serverUrl: string = (
    server === 'auth' ? process.env.AUTH_SERVER_URL : process.env.MESSENGER_SERVER_URL
  ) as string;
  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
  const response: [T | { error: string }, Response] | Error = yield call(() =>
    createFetch<T>({
      method,
      href: serverUrl + href,
      body: body ?? undefined,
    }),
  );
  const isActionExist = action !== undefined;
  const isResponseCrashed = response instanceof Error;
  const isResponseOkCrashed = !isResponseCrashed && !response[1].ok;
  const isResponseContainsErrorMessage =
    // @ts-ignore
    isResponseOkCrashed && !!response[0]?.error;
  if (isResponseCrashed) {
    yield put(setErrorsState(response.message));
  }
  if (isResponseOkCrashed) {
    if (isResponseContainsErrorMessage) {
      //@ts-ignore
      yield put(setErrorsState(response[0]?.error as string));
    } else {
      yield put(setErrorsState(`Error: ${response[1].statusText}`));
    }
  }
  if (isActionExist) {
    if (isDataInAction) {
      // @ts-ignore
      // eslint-disable-next-line @typescript-eslint/no-unsafe-argument
      yield action(response[0]);
    } else {
      yield action();
    }
  }
  yield put(setLoadingState(false));
  return !isResponseCrashed && !isResponseContainsErrorMessage ? response[0] : { error: true };
}

export { sagaHandling };
