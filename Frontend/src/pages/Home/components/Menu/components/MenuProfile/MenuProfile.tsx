import React, { ChangeEvent, Dispatch, SetStateAction, useEffect } from 'react';
import cn from 'classnames';
import { useSelector } from 'react-redux';
import { useActions } from '@/src/hooks/reduxHooks';
import { currentUserProfileSelector } from '@/src/reduxjs/profile/selectors';
import ModalWindow from '@/src/components/ModalWindow';
import MenuProfilePicture from './components/MenuProfilePicture';
import MenuProfileTag from './components/MenuProfileTag';
import MenuProfilePhoneNumber from './components/MenuProfilePhoneNumber';
import s from './MenuProfile.module.scss';

interface Props {
  setIsProfileOpen: Dispatch<SetStateAction<boolean>>;
}

const MenuProfile = ({ setIsProfileOpen }: Props): React.ReactElement => {
  const profile = useSelector(currentUserProfileSelector);
  const isProfileLoading = !profile?.name;
  const {
    getCurrentUserProfile,
    setProfileBio,
    setMessagesState,
    setProfileNickName,
    setChangedProfileInfo,
  } = useActions();

  const handleBioUnfocused = (e: ChangeEvent<HTMLInputElement>) => {
    const inputValue = e.target.value;
    const formattedBio = inputValue.replace(/\s+/gm, ' ').trim();
    const isBioTooLong = formattedBio && formattedBio.length > 100;
    const isBioAlreadyTaken = profile?.status === formattedBio;
    const isBioValid = !isBioTooLong && !isBioAlreadyTaken;
    if (isBioTooLong) {
      return setMessagesState({ error: 'Bio is too long' });
    }
    if (isBioValid) {
      setProfileBio({ property: formattedBio });
      setChangedProfileInfo({ property: 'status', value: formattedBio });
    }
  };

  const handleNicknameUnfocused = (e: ChangeEvent<HTMLInputElement>) => {
    const inputValue = e.target.value;
    const formattedNickName = inputValue.replace(/\s+/gm, ' ').trim();
    const isNickNameEmpty = !formattedNickName;
    const isNickNameTooLong = formattedNickName.length > 100;
    const isNicknameAlreadyTaken = profile?.name === formattedNickName;
    const isNickNameValid = !isNickNameEmpty && !isNickNameTooLong && !isNicknameAlreadyTaken;
    if (isNickNameEmpty) {
      return setMessagesState({ error: 'Nickname is empty' });
    }
    if (isNickNameTooLong) {
      return setMessagesState({ error: 'Nickname is too long' });
    }
    if (isNickNameValid) {
      setProfileNickName({ property: formattedNickName });
      setChangedProfileInfo({ property: 'name', value: formattedNickName });
    }
  };

  const handleEnterPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.currentTarget.blur();
    }
  };

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
      {isProfileLoading ? (
        <div className={s.loadingCircle} />
      ) : (
        <div className={s.root}>
          <div className={s.profileHeder}>
            <MenuProfilePicture profile={profile} setChangedProfileInfo={setChangedProfileInfo} />
            <input
              className={s.name}
              type="text"
              placeholder="Name"
              defaultValue={profile?.name}
              onBlur={handleNicknameUnfocused}
              onKeyDown={handleEnterPress}
            />
          </div>
          <div className={s.section}>
            <input
              className={cn(s.button, s.input)}
              type="text"
              placeholder="Bio"
              defaultValue={profile?.status ?? ''}
              onBlur={handleBioUnfocused}
              onKeyDown={handleEnterPress}
            />
            <MenuProfileTag
              profile={profile}
              setChangedProfileInfo={setChangedProfileInfo}
              setMessagesState={setMessagesState}
            />
            <MenuProfilePhoneNumber
              profile={profile}
              setChangedProfileInfo={setChangedProfileInfo}
              setMessagesState={setMessagesState}
            />
          </div>
        </div>
      )}
    </ModalWindow>
  );
};

export default MenuProfile;
