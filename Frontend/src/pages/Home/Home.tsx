'use client';
import React from 'react';
import { useRouter } from 'next/navigation';
import { useActions } from '@/src/hooks/reduxHooks';
import styles from './Home.module.scss';

const Home = (): React.ReactNode => {
  const { logout } = useActions();
  const router = useRouter();

  const handleLogoutClick = () => {
    logout();
    router.push('/auth/login');
  };

  return (
    <div className={styles.root}>
      Home
      <button onClick={handleLogoutClick}>Logout</button>
    </div>
  );
};

export default Home;
