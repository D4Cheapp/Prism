type CreateFetchPropsType = {
  method: 'GET' | 'POST' | 'PATCH' | 'DELETE';
  href: string;
  body?: object;
};

const createFetch = async <T>({
  method,
  href,
  body,
}: CreateFetchPropsType): Promise<[T, Response] | Error> => {
  const parts = `; ${document.cookie}`.split('; ');
  const cookie: string | null = parts.filter((part) => part.startsWith('SESSION'))[0];
  const init: RequestInit = {
    method,
    credentials: 'include',
    headers: {
      Cookie: cookie,
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
  };
  if (body) {
    init.body = JSON.stringify(body);
  }
  return await fetch(href, init)
    .then((response: Response) => Promise.all([response.json(), response]))
    .catch((error: Error) => error);
};

export { createFetch };
