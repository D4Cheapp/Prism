import React from 'react';
import Image from 'next/image';
import cn from 'classnames';
import { useTheme } from '@/src/hooks/useTheme';
import s from './HideButton.module.scss';

interface Props {
  isHide: boolean;
  toggleHide: (value: boolean) => void;
  styles?: string;
}

const HideButton = ({ isHide, styles, toggleHide }: Props): React.ReactElement => {
  const theme = useTheme();

  const handleClick = () => {
    toggleHide(!isHide);
  };

  return (
    <div onClick={handleClick} className={cn(s.root, styles)}>
      <Image
        className={s.eye}
        src={`/icons/menu/${theme[0]}/eye.svg`}
        alt="Hide button"
        width={35}
        height={35}
      />
      <div className={cn(s.eyeLine, { [s.isHide]: isHide })} />
    </div>
  );
};

export default HideButton;
