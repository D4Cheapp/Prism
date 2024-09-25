import React from 'react';
import s from './Dialog.module.scss';

interface Props {}

const Dialog = ({}: Props): React.ReactElement => {
  return <section className={s.dialog}>Dialog works!</section>;
};

export default Dialog;
