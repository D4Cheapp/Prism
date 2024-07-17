'use client';
import cn from 'classnames';
import { useAppSelector } from '@/src/hooks/reduxHooks';
import { loadingSelector } from '@/src/reduxjs/base/selectors';
import s from './CustomButton.module.scss';

interface Props {
  text: string;
  withLoader?: boolean;
  styles?: string;
  type?: 'button' | 'submit' | 'reset';
  onClick?: () => void;
}

const CustomButton = ({
  text,
  styles,
  type = 'button',
  withLoader = false,
  onClick,
}: Props): React.ReactNode => {
  const isLoading = useAppSelector(loadingSelector);

  return (
    <button
      className={cn(s.submitButton, styles)}
      type={type}
      disabled={isLoading}
      onClick={onClick}
    >
      {isLoading && withLoader ? <div className={s.loader} /> : text}
    </button>
  );
};

export default CustomButton;
