import React from 'react';
import cn from 'classnames';
import s from './ModalButtons.module.scss';

interface Props {
  onConfirmClick?: () => void;
  onCloseWindowClick: () => void;
  buttonInfo: {
    confirmTitle?: string;
    withGoHomeButton?: boolean;
    withConfirmButton?: boolean;
  };
}

const ModalButtons = ({
  onConfirmClick,
  onCloseWindowClick,
  buttonInfo,
}: Props): React.ReactNode => {
  return (
    <div className={s.formButtons}>
      {buttonInfo.withConfirmButton && (
        <button
          type="button"
          className={cn(s.formButton, s.confirmButton)}
          onClick={onConfirmClick}
        >
          {buttonInfo?.confirmTitle ?? 'Confirm'}
        </button>
      )}
      <button
        type="button"
        className={cn(s.formButton, s.cancelButton)}
        onClick={onCloseWindowClick}
      >
        Отмена
      </button>
    </div>
  );
};

export default ModalButtons;
