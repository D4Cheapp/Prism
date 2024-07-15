import React, { useEffect, useState } from 'react';
import cn from 'classnames';
import s from './ErrorMessage.module.scss';

interface Props {
  error: string;
  index: number;
  onCloseClick: (index: number) => void;
}

const ErrorMessage = ({ error, index, onCloseClick }: Props): null | React.ReactNode => {
  const [isFaded, setIsFaded] = useState(false);

  const handleErrorCloseClick = () => {
    onCloseClick(index);
  };

  useEffect(() => {
    const fadeTimer = setTimeout(() => setIsFaded(true), 3500);
    const timer = setTimeout(() => onCloseClick(index), 4000);
    return () => {
      clearTimeout(fadeTimer);
      clearTimeout(timer);
    };
  }, [index, onCloseClick]);

  return (
    <div className={cn(s.errorMessage, { [s.fadeAnimation]: isFaded })}>
      <p className={s.error}>{error}</p>
      <button className={s.close} onClick={handleErrorCloseClick} />
    </div>
  );
};

export default ErrorMessage;
