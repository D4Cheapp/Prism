import React, { useState } from 'react';
import cn from 'classnames';
import Eye from 'public/icons/menu/eye.svg';
import HideEye from 'public/icons/menu/hideEye.svg';
import s from './HideButton.module.scss';

interface Props {
  isHide: boolean;
  toggleHide: (value: boolean) => void;
  styles?: string;
}

const HideButton = ({ isHide, styles, toggleHide }: Props): React.ReactElement => {
  const [isHidePressed, setIsHidePressed] = useState(false);

  const handleClick = () => {
    setIsHidePressed(true);
    setTimeout(() => toggleHide(!isHide), 200);
  };

  return (
    <div onClick={handleClick} className={cn(s.root, styles)}>
      {isHide ? (
        <HideEye className={cn(s.eye, { [s.isHide]: isHidePressed })} width={35} height={35} />
      ) : (
        <Eye className={cn(s.eye, { [s.isHide]: isHidePressed })} width={35} height={35} />
      )}
    </div>
  );
};

export default HideButton;
