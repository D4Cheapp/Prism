'use client';
import React, { Dispatch, SetStateAction, useCallback, useEffect, useState } from 'react';
import cn from 'classnames';
import ModalButtons from './ModalButtons';
import s from './ModalWindow.module.scss';

interface Props {
  title: string;
  setIsActive: Dispatch<SetStateAction<boolean>>;
  onConfirmClick?: (() => void) | (() => Promise<void>);
  children?: React.ReactNode;
  buttonInfo?: {
    confirmTitle?: string;
    withConfirmButton?: boolean;
  };
}

const ModalWindow = ({
  setIsActive,
  onConfirmClick,
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

  useEffect(() => {
    addEventListener('keydown', handleEscapeKeyClick);
    return () => removeEventListener('keydown', handleEscapeKeyClick);
  }, [handleEscapeKeyClick]);

  return (
    <aside className={cn(s.root, { [s.disappearing]: isCloseButtonClicked })}>
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
            onCloseWindowClick={handleCloseWindowClick}
            buttonInfo={buttonInfo}
          />
        )}
      </div>
    </aside>
  );
};

export default ModalWindow;
