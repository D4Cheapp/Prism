'use client';
import React, { useEffect, useState } from 'react';
import { Provider } from 'react-redux';
import cn from 'classnames';
import { store } from '@/src/reduxjs';
import { ThemeContext } from './ThemeContext';

interface Props {
  children: React.ReactNode;
}

const Providers = ({ children }: Props): React.ReactNode => {
  const [theme, setTheme] = useState<'dark' | 'light'>('dark');

  useEffect(() => {
    const localTheme = localStorage.getItem('theme');
    const isThemeExist = (localTheme === 'dark' || localTheme === 'light') && localTheme !== theme;
    if (isThemeExist) {
      setTheme(localTheme);
    } else {
      localStorage.setItem('theme', 'dark');
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <body className={cn('body', theme)}>
      <ThemeContext.Provider value={{ theme, handleChangeTheme: setTheme }}>
        <Provider store={store}>{children}</Provider>
      </ThemeContext.Provider>
    </body>
  );
};

export default Providers;
