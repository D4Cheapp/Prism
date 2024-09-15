'use client';
import { useEffect, useState } from 'react';
import Image from 'next/image';
import cn from 'classnames';
import { useTheme } from '../../hooks/useTheme';
import s from './ChangeThemeIcon.module.scss';

interface Props {
  size?: number;
}

const ChangeThemeIcon = ({ size }: Props) => {
  const [theme, setTheme] = useTheme();
  const [isFade, setIsFade] = useState(false);
  const checkImageSource =
    theme === 'light' ? '/icons/themeSwitch/moon.svg' : '/icons/themeSwitch/sun.svg';
  const [imageSource, setImageSource] = useState(checkImageSource);

  useEffect(() => {
    setIsFade(true);
    setImageSource(checkImageSource);
    const timer = setTimeout(() => {
      setIsFade(false);
    }, 200);
    return () => clearTimeout(timer);
  }, [checkImageSource, theme]);

  return (
    <Image
      className={cn(s.icon, {
        [s.rightFade]: isFade && theme === 'dark',
        [s.leftFade]: isFade && theme === 'light',
      })}
      onClick={setTheme}
      priority={false}
      src={imageSource}
      alt="Theme switch"
      width={size ?? 40}
      height={size ?? 40}
    />
  );
};

export default ChangeThemeIcon;
