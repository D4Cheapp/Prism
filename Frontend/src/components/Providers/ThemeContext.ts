'use client';
import { createContext, Dispatch, SetStateAction } from 'react';

type ThemeContextType = {
  theme: 'dark' | 'light';
  handleChangeTheme: Dispatch<SetStateAction<'dark' | 'light'>>;
};
//@ts-ignore
export const ThemeContext = createContext<ThemeContextType>({});
