'use client';
import cn from 'classnames';
import { Dispatch, SetStateAction, useState } from 'react';
import Profile from 'public/icons/menu/profile.svg';
import Friends from 'public/icons/menu/friends.svg';
import Chats from 'public/icons/menu/chats.svg';
import Settings from 'public/icons/menu/settings.svg';
import Groups from 'public/icons/menu/groups.svg';
import { SelectedCategoryType } from '@/src/types/profileReceiveTypes';
import MenuSettings from './components/MenuSettings';
import MenuProfile from './components/MenuProfile';
import s from './Menu.module.scss';

interface Props {
  selectedCategory: SelectedCategoryType;
  setSelectedCategory: Dispatch<SetStateAction<SelectedCategoryType>>;
}

const Menu = ({ selectedCategory, setSelectedCategory }: Props): React.ReactElement => {
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  const handleSettingsClick = () => setIsSettingsOpen(true);

  const handleProfileClick = () => setIsProfileOpen(true);

  const handleFriendsClick = () => setSelectedCategory('friends');

  const handleGroupsClick = () => setSelectedCategory('groups');

  const handleChatsClick = () => setSelectedCategory('chats');

  return (
    <aside className={s.menu}>
      <div className={s.buttonsContainer}>
        <button title="Profile" className={s.imageButton} onClick={handleProfileClick}>
          <Profile className={s.icon} width={25} height={25} />
        </button>
        {isProfileOpen && <MenuProfile setIsProfileOpen={setIsProfileOpen} />}
        <button
          title="Friends"
          className={cn(s.imageButton, { [s.active]: selectedCategory === 'friends' })}
          onClick={handleFriendsClick}
        >
          <Friends className={s.icon} width={25} height={25} />
        </button>
        <button
          title="Groups"
          className={cn(s.imageButton, { [s.active]: selectedCategory === 'groups' })}
          onClick={handleGroupsClick}
        >
          <Groups className={s.icon} width={25} height={25} />
        </button>
        <button
          title="Chats"
          className={cn(s.imageButton, { [s.active]: selectedCategory === 'chats' })}
          onClick={handleChatsClick}
        >
          <Chats className={s.icon} width={25} height={25} />
        </button>
      </div>
      <div className={cn(s.buttonsContainer, s.settings)}>
        <button className={cn(s.imageButton, s.settings)} onClick={handleSettingsClick}>
          <Settings className={s.icon} width={35} height={35} />
        </button>
        {isSettingsOpen && <MenuSettings setIsSettingsOpen={setIsSettingsOpen} />}
      </div>
    </aside>
  );
};

export default Menu;
