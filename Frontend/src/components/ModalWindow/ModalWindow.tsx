'use client';
import React, {Dispatch, SetStateAction, useCallback, useEffect} from 'react';
import cn from 'classnames';
import ModalButtons from './ModalButtons';
import s from './ModalWindow.module.scss';

interface Props {
  setIsActive: Dispatch<SetStateAction<boolean>>;
  onConfirmClick?: () => void;
  children?: React.ReactNode;
  title: string;
  buttonInfo: {
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
  const handleCloseWindowClick = useCallback(() => {
    setIsActive(false);
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
      <aside className={s.root}>
        <div className={s.background} onClick={handleCloseWindowClick}/>
        <div
            className={cn(s.componentFrom, {
              [s.invisibleContent]: !children,
            })}
        >
          <div className={s.formHeader}>
            <h1 className={s.title}>{title}</h1>
            <button className={s.closeButton} onClick={handleCloseWindowClick}/>
          </div>
          {children}
          <ModalButtons
              onConfirmClick={onConfirmClick}
              onCloseWindowClick={handleCloseWindowClick}
              buttonInfo={buttonInfo}
          />
        </div>
      </aside>
  );
};

export default ModalWindow;
