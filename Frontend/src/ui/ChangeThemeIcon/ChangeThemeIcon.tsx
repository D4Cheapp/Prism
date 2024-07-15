'use client';
import { memo } from 'react';
import Image from 'next/image';
import { useTheme } from '../../hooks/useTheme';
import s from './ChangeThemeIcon.module.scss';

const ChangeThemeIcon = memo(function ChangeThemeIcon() {
  const [theme, setTheme] = useTheme();
  return (
    <Image
      className={s.icon}
      onClick={setTheme}
      priority={false}
      src={theme === 'light' ? '/icons/themeSwitch/moon.svg' : '/icons/themeSwitch/sun.svg'}
      alt="Theme switch"
      width={40}
      height={40}
    />
  );
});

export default ChangeThemeIcon;
