'use client';
import React, { Dispatch, SetStateAction, useCallback, useEffect, useState } from 'react';
import cn from 'classnames';
import ModalButtons from './ModalButtons';
import s from './ModalWindow.module.scss';

interface Props {
  title: string;
  setIsActive: Dispatch<SetStateAction<boolean>>;
  onConfirmClick?: (() => void) | (() => Promise<void>);
  onSecondButtonClick?: (() => void) | (() => Promise<void>);
  children?: React.ReactNode;
  buttonInfo?: {
    confirmTitle?: string;
    withConfirmButton?: boolean;
    secondButtonTitle?: string;
  };
}

const ModalWindow = ({
  setIsActive,
  onConfirmClick,
  onSecondButtonClick,
  children,
  title,
  buttonInfo,
}: Props): React.ReactNode => {
  const [isCloseButtonClicked, setIsCloseButtonClicked] = useState(false);

  const handleCloseWindowClick = useCallback(() => {
    setIsCloseButtonClicked(true);
    const timer = setTimeout(() => setIsActive(false), 200);
    return () => clearTimeout(timer);
  }, [setIsActive]);

  const handleEscapeKeyClick = useCallback(
    (event: KeyboardEvent) => {
      const isEscapePressed = event.key === 'Escape';
      if (isEscapePressed) {
        handleCloseWindowClick();
      }
    },
    [handleCloseWindowClick],
  );

  const handleEnterPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    const isConfirmValid = e.key === 'Enter' && onConfirmClick;
    if (isConfirmValid) {
      void onConfirmClick();
    }
  };

  useEffect(() => {
    addEventListener('keydown', handleEscapeKeyClick);
    return () => removeEventListener('keydown', handleEscapeKeyClick);
  }, [handleEscapeKeyClick]);

  return (
    <aside
      className={cn(s.root, { [s.disappearing]: isCloseButtonClicked })}
      onKeyDown={handleEnterPress}
    >
      <div className={s.background} onClick={handleCloseWindowClick} />
      <div
        className={cn(s.componentFrom, {
          [s.invisibleContent]: !children,
        })}
      >
        <div className={s.formHeader}>
          <h1 className={s.title}>{title}</h1>
          <button title="Close" className={s.closeButton} onClick={handleCloseWindowClick} />
        </div>
        {children}
        {buttonInfo && (
          <ModalButtons
            onConfirmClick={onConfirmClick}
            onSecondButtonClick={onSecondButtonClick}
            onCloseWindowClick={handleCloseWindowClick}
            buttonInfo={buttonInfo}
          />
        )}
      </div>
    </aside>
  );
};

export default ModalWindow;
