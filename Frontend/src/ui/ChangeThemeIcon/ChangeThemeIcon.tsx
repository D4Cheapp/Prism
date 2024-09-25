'use client';
import { useState } from 'react';
import cn from 'classnames';
import Moon from 'public/icons/themeSwitch/moon.svg';
import Sun from 'public/icons/themeSwitch/sun.svg';
import { useTheme } from '../../hooks/useTheme';
import s from './ChangeThemeIcon.module.scss';

interface Props {
  size?: number;
}

const ChangeThemeIcon = ({ size }: Props) => {
  const [theme, toggleTheme] = useTheme();
  const [isFade, setIsFade] = useState(false);

  const handleThemeToggle = () => {
    setIsFade(true);
    setTimeout(() => {
      setIsFade(false);
      toggleTheme();
    }, 100);
  };

  return (
    <>
      {theme === 'light' ? (
        <Moon
          className={cn(s.icon, { [s.rightFade]: isFade })}
          onClick={handleThemeToggle}
          width={size ?? 24}
          height={size ?? 24}
        />
      ) : (
        <Sun
          className={cn(s.icon, { [s.leftFade]: isFade })}
          onClick={handleThemeToggle}
          width={size ?? 24}
          height={size ?? 24}
        />
      )}
    </>
  );
};

export default ChangeThemeIcon;
