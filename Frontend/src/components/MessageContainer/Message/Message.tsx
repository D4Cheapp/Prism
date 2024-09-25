import React, { useEffect, useState } from 'react';
import cn from 'classnames';
import s from './Message.module.scss';

interface Props {
  message: { error?: string; info?: string; id: number };
  index: number;
  onCloseClick: (index: number) => void;
}

const Message = ({ message, index, onCloseClick }: Props): null | React.ReactNode => {
  const [isFaded, setIsFaded] = useState(false);
  const isError = !!message.error;

  const handleMessageCloseClick = () => {
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
    <div className={cn(s.messageContainer, { [s.fadeAnimation]: isFaded, [s.infoMessage]: !isError, [s.errorMessage]: isError })}>
      <p className={s.message}>{ isError ? message.error : message.info}</p>
      <button title='Close' className={s.close} onClick={handleMessageCloseClick} />
    </div>
  );
};

export default Message;
