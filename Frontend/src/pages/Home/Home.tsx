'use client';
import React from 'react';
import { useActions } from '@/src/hooks/reduxHooks';
import styles from './Home.module.scss';

const Home = (): React.ReactNode => {
  const { logout } = useActions();
  return (
    <div className={styles.root}>
      Home
      <button onClick={() => logout()}>Logout</button>
    </div>
  );
};

export default Home;
