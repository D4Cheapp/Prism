import React from 'react';
import cn from 'classnames';
import { Field } from 'formik';
import s from './CustomInputButton.module.scss';

interface Props {
  name: string;
  type: 'checkbox' | 'radio';
  isFormInput?: boolean;
  className: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  id?: string;
  defaultChecked?: boolean;
}

function CustomInputButton({
  name,
  type,
  isFormInput,
  className,
  onChange,
  id,
  defaultChecked,
}: Props): React.ReactNode {
  const inputInfo = {
    className: cn({ [s.checkbox]: type === 'checkbox', [s.radioButton]: type === 'radio' }),
    type,
    id: id ? id : name,
    name,
  };
  return (
    <>
      {isFormInput ? (
        <Field {...inputInfo} />
      ) : (
        <input {...inputInfo} onChange={onChange} defaultChecked={defaultChecked} />
      )}
      <div
        className={cn(
          {
            [s.customCheckbox]: type === 'checkbox',
            [s.customRadioButton]: type === 'radio',
          },
          className,
        )}
      />
    </>
  );
}

export default CustomInputButton;
