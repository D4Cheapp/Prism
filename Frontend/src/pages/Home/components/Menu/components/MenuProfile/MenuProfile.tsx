import React, { Dispatch, SetStateAction, useEffect } from 'react';
import { useSelector } from 'react-redux';
import Image from 'next/image';
import cn from 'classnames';
import { useActions } from '@/src/hooks/reduxHooks';
import ModalWindow from '@/src/components/ModalWindow';
import Profile from 'public/icons/menu/profile.svg';
import { currentUserProfileSelector } from '@/src/reduxjs/messenger/selectors';
import { loadingSelector } from '@/src/reduxjs/base/selectors';
import s from './MenuProfile.module.scss';

interface Props {
  setIsProfileOpen: Dispatch<SetStateAction<boolean>>;
}

const MenuProfile = ({ setIsProfileOpen }: Props): React.ReactElement => {
  const profile = useSelector(currentUserProfileSelector);
  const isLoading = useSelector(loadingSelector);
  const { getCurrentUserProfile } = useActions();

  useEffect(() => {
    const isProfileNotFetched = profile === null;
    const isRequestFailed = profile === undefined;
    if (isProfileNotFetched) {
      getCurrentUserProfile();
    }
    if (isRequestFailed) {
      setIsProfileOpen(false);
    }
  }, [getCurrentUserProfile, profile, setIsProfileOpen]);

  return (
    <ModalWindow setIsActive={setIsProfileOpen} title="Profile">
      {isLoading ? (
        <div className={s.loadingCircle} />
      ) : (
        <div className={s.root}>
          <div className={s.profileHeder}>
            {profile?.profilePicture ? (
              <Image
                className={s.icon}
                src={`data:image/jpeg;base64,${profile.profilePicture}`}
                alt="profile"
              />
            ) : (
              <Profile className={s.icon} />
            )}
            <input className={s.name} type="text" placeholder="Name" defaultValue={profile?.name} />
          </div>
          <div className={s.section}>
            <input
              className={cn(s.button, s.input)}
              type="text"
              placeholder="Bio"
              defaultValue={profile?.status ?? ''}
            />
            <button className={s.button}>
              Tag <p className={s.parameter}>{profile?.tag}</p>
            </button>
            <button className={s.button}>
              Phone number <p className={s.parameter}>{profile?.phoneNumber ?? '-'}</p>
            </button>
          </div>
        </div>
      )}
    </ModalWindow>
  );
};

export default MenuProfile;
