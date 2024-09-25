type CreateFetchPropsType = {
  method: 'GET' | 'POST' | 'PATCH' | 'DELETE';
  href: string;
  body?: object;
  multipart?: boolean;
};

const createFetch = async <T>({
  method,
  href,
  body,
  multipart,
}: CreateFetchPropsType): Promise<[T, Response] | Error> => {
  const parts = `; ${document.cookie}`.split('; ');
  const cookie: string | null = parts.filter((part) => part.startsWith('SESSION'))[0];
  const isBodyExist = body && !multipart;
  const isBodyMultipartExist = body && multipart;
  const init: RequestInit = {
    method,
    credentials: 'include',
    headers: {
      Cookie: cookie,
    },
  };
  if (isBodyExist) {
    init.body = JSON.stringify(body);
  }
  if (isBodyMultipartExist) {
    //@ts-ignore
    init.body = body;
  }
  return await fetch(href, init)
    .then((response: Response) => Promise.all([response.json(), response]))
    .catch((error: Error) => error);
};

export { createFetch };
