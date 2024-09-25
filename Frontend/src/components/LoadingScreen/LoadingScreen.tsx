import React from 'react';
import s from './LoadingScreen.module.scss';

const LoadingScreen = (): React.ReactNode => {
  return (
    <div className={s.root}>
      <div className={s.loading} />
    </div>
  );
};

export default LoadingScreen;
