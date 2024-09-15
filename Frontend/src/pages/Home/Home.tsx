'use client';
import React, { useState } from 'react';
import Dialog from './components/Dialog';
import MessengerLists from './components/MessengerLists';
import Menu from './components/Menu';
import s from './Home.module.scss';

const Home = (): React.ReactNode => {
  const [isDialogOpen, setIsOpenDialog] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<'friends' | 'chats' | 'groups'>('chats');

  return (
    <div className={s.root}>
      <Menu selectedCategory={selectedCategory} setSelectedCategory={setSelectedCategory} />
      <MessengerLists setIsOpenDialog={setIsOpenDialog} isDialogOpen={isDialogOpen} />
      {isDialogOpen && <Dialog />}
    </div>
  );
};

export default Home;
