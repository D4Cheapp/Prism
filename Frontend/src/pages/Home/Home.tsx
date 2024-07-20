'use client';
import React, { useEffect, useState } from 'react';
import Menu from './components/Menu';
import MessengerLists from './components/MessengerLists';
import Dialog from './components/Dialog';
import s from './Home.module.scss';

const Home = (): React.ReactNode => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setIsDialogOpen(true), 500);
    return () => clearTimeout(timer);
  }, []);

  return (
    <div className={s.root}>
      <Menu />
      {isDialogOpen && <Dialog />}
      <MessengerLists isDialogOpen={isDialogOpen} />
    </div>
  );
};

export default Home;
