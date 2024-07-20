import React from 'react';
import cn from 'classnames';
import { useAppSelector } from '@/src/hooks/reduxHooks';
import { loadingSelector } from '@/src/reduxjs/base/selectors';
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
  const isLoading = useAppSelector(loadingSelector);

  return (
    <div className={s.formButtons}>
      {buttonInfo.withConfirmButton && (
        <button
          type="button"
          className={cn(s.formButton, s.confirmButton)}
          onClick={onConfirmClick}
          disabled={isLoading}
        >
          {isLoading ? <div className={s.loader} /> : (buttonInfo?.confirmTitle ?? 'Confirm')}
        </button>
      )}
      <button
        type="button"
        className={cn(s.formButton, s.cancelButton)}
        onClick={onCloseWindowClick}
      >
        Cancel
      </button>
    </div>
  );
};

export default ModalButtons;
