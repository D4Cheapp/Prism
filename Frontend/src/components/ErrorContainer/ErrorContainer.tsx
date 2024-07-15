'use client';
import React, { useCallback } from 'react';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import { errorSelector } from '@/src/reduxjs/base/selectors';
import ErrorMessage from './ErrorMessage';
import s from './ErrorContainer.module.scss';

const ErrorContainer = (): React.ReactNode => {
  const errorsState = useAppSelector(errorSelector);
  const isErrorsEmpty = errorsState.length === 0;
  const { deleteErrorState } = useActions();

  const handleCloseClick = useCallback((index: number) => {
    deleteErrorState(index);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (isErrorsEmpty) {
    return null;
  }

  return (
    <div className={s.container}>
      {errorsState.map((error) => (
        <ErrorMessage
          key={error.id}
          error={error.error}
          index={error.id}
          onCloseClick={handleCloseClick}
        />
      ))}
    </div>
  );
};

export default ErrorContainer;
