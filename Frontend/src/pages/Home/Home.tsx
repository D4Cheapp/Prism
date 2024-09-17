'use client';
import React, { useState } from 'react';
import { SelectedCategoryType } from '@/src/types/messengerTypes';
import Dialog from './components/Dialog';
import Menu from './components/Menu';
import MessengerLists from './components/MessengerLists';
import s from './Home.module.scss';

const Home = (): React.ReactNode => {
  const [isDialogOpen, setIsOpenDialog] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<SelectedCategoryType>('chats');

  return (
    <div className={s.root}>
      <Menu selectedCategory={selectedCategory} setSelectedCategory={setSelectedCategory} />
      <MessengerLists selectedCategory={selectedCategory} setIsOpenDialog={setIsOpenDialog} />
      {isDialogOpen && <Dialog />}
    </div>
  );
};

export default Home;
