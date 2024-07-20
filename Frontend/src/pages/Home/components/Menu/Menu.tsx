'use client';
import Image from 'next/image';
import { useCallback, useState } from 'react';
import { useTheme } from '@/src/hooks/useTheme';
import MenuSettings from './MenuSettings';
import s from './Menu.module.scss';

interface Props {}

const Menu = ({}: Props): React.ReactElement => {
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [theme, setTheme] = useTheme();

  const handleSettingsClick = useCallback(() => {
    setIsSettingsOpen(true);
  }, []);

  return (
    <aside className={s.menu}>
      <div className={s.mainButtons}>
        <button className={s.imageButton}>
          <Image src={`/icons/menu/${theme}/profile.svg`} alt="profile" width={50} height={50} />
        </button>
        <button className={s.imageButton}>
          <Image src={`/icons/menu/${theme}/friends.svg`} alt="friends" width={50} height={50} />
        </button>
        <button className={s.imageButton}>
          <Image src={`/icons/menu/${theme}/groups.svg`} alt="groups" width={50} height={50} />
        </button>
        <button className={s.imageButton}>
          <Image src={`/icons/menu/${theme}/chats.svg`} alt="chats" width={50} height={50} />
        </button>
      </div>
      <button className={s.imageButton} onClick={handleSettingsClick}>
        <Image src={`/icons/menu/${theme}/gear.svg`} alt="gear" width={50} height={50} />
      </button>
      {isSettingsOpen && <MenuSettings setTheme={setTheme} setIsSettingsOpen={setIsSettingsOpen} />}
    </aside>
  );
};

export default Menu;
