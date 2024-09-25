import { useContext } from 'react';
import { ThemeContext } from '../components/Providers/ThemeContext';

export const useTheme = (): ['dark' | 'light', () => void] => {
  const { theme, handleChangeTheme } = useContext(ThemeContext);

  const setTheme = () => {
    if (theme === 'dark') {
      handleChangeTheme('light');
      localStorage.setItem('theme', 'light');
    } else {
      handleChangeTheme('dark');
      localStorage.setItem('theme', 'dark');
    }
  };

  return [theme, setTheme];
};
