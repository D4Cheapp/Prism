import React, { ChangeEvent, FocusEventHandler } from 'react';
import InputMask from 'react-input-mask';
import cn from 'classnames';
import { Field } from 'formik';
import s from './CustomInput.module.scss';

interface Props {
  name: string;
  reference?: React.RefObject<HTMLInputElement>;
  placeholder?: string;
  isFormInput?: boolean;
  label?: string;
  readOnly?: boolean;
  autoFocus?: boolean;
  type?: 'text' | 'password' | 'number' | 'email' | 'tel';
  value?: string;
  mask?: string;
  onChange?: (e: ChangeEvent<HTMLInputElement>) => void;
  onBlur?: FocusEventHandler;
  id?: string;
  defaultValue?: string;
  classNames?: {
    input?: string;
    title?: string;
  };
}

function CustomInput({
  name,
  placeholder,
  isFormInput,
  label,
  readOnly,
  autoFocus,
  mask,
  type = 'text',
  onChange,
  value,
  onBlur,
  reference,
  defaultValue,
  id,
  classNames,
}: Props): React.ReactNode {
  const inputInfo = {
    className: cn(s.input, classNames ? classNames.input : ''),
    type,
    id: id ? id : name,
    name,
    placeholder,
    autoFocus,
    onBlur,
    onChange,
    readOnly,
  };
  return (
    <div className={s.root}>
      {isFormInput ? (
        <Field {...inputInfo} defaultValue={defaultValue} />
      ) : mask ? (
        <InputMask value={value} mask={mask} {...inputInfo} />
      ) : (
        <input {...inputInfo} ref={reference} defaultValue={defaultValue} />
      )}
      {label && (
        <label className={cn(s.label, classNames ? classNames.title : '')} htmlFor={id ? id : name}>
          {label}
        </label>
      )}
    </div>
  );
}

export default CustomInput;
