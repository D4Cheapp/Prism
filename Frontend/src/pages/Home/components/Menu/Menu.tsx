'use client';
import cn from 'classnames';
import { Dispatch, SetStateAction, useCallback, useState } from 'react';
import Profile from 'public/icons/menu/profile.svg';
import Friends from 'public/icons/menu/friends.svg';
import Chats from 'public/icons/menu/chats.svg';
import Settings from 'public/icons/menu/settings.svg';
import Groups from 'public/icons/menu/groups.svg';
import { SelectedCategoryType } from '@/src/types/messengerTypes';
import MenuSettings from './MenuSettings';
import s from './Menu.module.scss';

interface Props {
  selectedCategory: SelectedCategoryType;
  setSelectedCategory: Dispatch<SetStateAction<SelectedCategoryType>>;
}

const Menu = ({ selectedCategory, setSelectedCategory }: Props): React.ReactElement => {
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);

  const handleSettingsClick = useCallback(() => {
    setIsSettingsOpen(true);
  }, []);

  const handleProfileClick = useCallback(() => {}, []);

  const handleFriendsClick = useCallback(() => {
    setSelectedCategory('friends');
  }, [setSelectedCategory]);

  const handleGroupsClick = useCallback(() => {
    setSelectedCategory('groups');
  }, [setSelectedCategory]);

  const handleChatsClick = useCallback(() => {
    setSelectedCategory('chats');
  }, [setSelectedCategory]);

  return (
    <aside className={s.menu}>
      <div className={s.buttonsContainer}>
        <button title="Profile" className={s.imageButton} onClick={handleProfileClick}>
          <Profile className={s.icon} width={25} height={25} />
        </button>
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
      </div>
      {isSettingsOpen && <MenuSettings setIsSettingsOpen={setIsSettingsOpen} />}
    </aside>
  );
};

export default Menu;
