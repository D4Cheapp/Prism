import React, { ChangeEvent, Dispatch, SetStateAction, useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import Image from 'next/image';
import cn from 'classnames';
import { useActions } from '@/src/hooks/reduxHooks';
import ModalWindow from '@/src/components/ModalWindow';
import Profile from 'public/icons/menu/profile.svg';
import { currentUserProfileSelector } from '@/src/reduxjs/profile/selectors';
import CustomInput from '@/src/ui/CustomInput';
import s from './MenuProfile.module.scss';

interface Props {
  setIsProfileOpen: Dispatch<SetStateAction<boolean>>;
}

const MenuProfile = ({ setIsProfileOpen }: Props): React.ReactElement => {
  const profile = useSelector(currentUserProfileSelector);
  const isProfileLoading = !profile?.name;
  const [isChangeTagWindowOpen, setIsChangeTagWindowOpen] = useState<boolean>(false);
  const [isChangePhoneNumberWindowOpen, setIsChangePhoneNumberWindowOpen] =
    useState<boolean>(false);
  const [phoneNumberInput, setPhoneNumberInput] = useState<string>('');
  const phoneNumberInputRef = useRef<HTMLInputElement>(null);
  const tagInputRef = useRef<HTMLInputElement>(null);
  const {
    getCurrentUserProfile,
    setProfileBio,
    setMessagesState,
    setProfileNickName,
    setChangedProfileInfo,
    setProfilePhoneNumber,
    setProfileTag,
  } = useActions();

  const handleProfilePictureClick = () => {};

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

  const handleTagClick = () => setIsChangeTagWindowOpen(true);

  const handleTagChangeConfirm = () => {
    const tag = tagInputRef.current?.value;
    const formattedTag = '@' + tag?.replace(/\s+/gm, ' ').replace(/@/gm, '').trim();
    const isTagTooLong = formattedTag && formattedTag.length > 100;
    const isTagAlreadyTaken = profile?.tag === formattedTag;
    const isTagValid = formattedTag && !isTagTooLong && !isTagAlreadyTaken;
    if (isTagTooLong) {
      return setMessagesState({ error: 'Tag is too long' });
    }
    if (isTagValid) {
      setProfileTag({ tag: formattedTag });
      setChangedProfileInfo({ property: 'tag', value: formattedTag });
      setIsChangeTagWindowOpen(false);
    }
  };

  const handlePhoneNumberClick = () => setIsChangePhoneNumberWindowOpen(true);

  const handlePhoneNumberChangeConfirm = () => {
    const phoneNumber = phoneNumberInputRef.current?.value;
    const isPhoneNumberFormatCorrect =
      /^\+?\d{1,3}[-.\s]?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{2}[-.\s]?\d{2}/.test(phoneNumber ?? '');
    const isPhoneNumberValid = phoneNumber && isPhoneNumberFormatCorrect;
    if (isPhoneNumberValid) {
      setProfilePhoneNumber({ property: phoneNumber });
      setChangedProfileInfo({ property: 'phoneNumber', value: phoneNumber });
      setIsChangePhoneNumberWindowOpen(false);
    } else {
      setMessagesState({ error: 'Phone number is invalid' });
    }
  };

  const handlePhoneNumberDeleteClick = () => {
    setProfilePhoneNumber({ property: '' });
    setChangedProfileInfo({ property: 'phoneNumber', value: '' });
    setIsChangePhoneNumberWindowOpen(false);
  };

  const handleEnterPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.currentTarget.blur();
    }
  };

  const handlePhoneNumberChange = (e: ChangeEvent<HTMLInputElement>) => {
    setPhoneNumberInput(e.target.value);
  };

  useEffect(() => {
    const isProfileNotFetched = profile === null;
    const isProfileFetched = profile !== null;
    const isRequestFailed = profile === undefined;
    if (isProfileNotFetched) {
      getCurrentUserProfile();
    }
    if (isProfileFetched) {
      setPhoneNumberInput(profile?.phoneNumber ?? '');
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
            {profile?.profilePicture ? (
              <Image
                className={s.icon}
                src={`data:image/jpeg;base64,${profile.profilePicture}`}
                alt="profile"
                onClick={handleProfilePictureClick}
              />
            ) : (
              <Profile className={s.icon} onClick={handleProfilePictureClick} />
            )}
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
            <button className={s.button} onClick={handleTagClick}>
              Tag <p className={s.parameter}>{profile?.tag}</p>
            </button>
            {isChangeTagWindowOpen && (
              <ModalWindow
                title="Tag"
                setIsActive={setIsChangeTagWindowOpen}
                onConfirmClick={handleTagChangeConfirm}
                buttonInfo={{ confirmTitle: 'Save', withConfirmButton: true }}
              >
                <CustomInput
                  name="tag"
                  autoFocus
                  placeholder="Tag"
                  reference={tagInputRef}
                  classNames={{ input: s.modalInput }}
                  defaultValue={profile?.tag.slice(1)}
                />
              </ModalWindow>
            )}
            <button className={s.button} onClick={handlePhoneNumberClick}>
              Phone number <p className={s.parameter}>{profile?.phoneNumber ?? '-'}</p>
            </button>
            {isChangePhoneNumberWindowOpen && (
              <ModalWindow
                title="Phone number"
                setIsActive={setIsChangePhoneNumberWindowOpen}
                onConfirmClick={handlePhoneNumberChangeConfirm}
                onSecondButtonClick={handlePhoneNumberDeleteClick}
                buttonInfo={{
                  confirmTitle: 'Save',
                  withConfirmButton: true,
                  secondButtonTitle: 'Delete',
                }}
              >
                <CustomInput
                  name="phoneNumber"
                  type="tel"
                  placeholder="+ 7 ( _ _ _ ) _ _ _ - _ _ - _ _"
                  mask="+7 (999) 999-99-99"
                  onChange={handlePhoneNumberChange}
                  value={phoneNumberInput}
                  reference={phoneNumberInputRef}
                  classNames={{ input: s.modalInput }}
                />
              </ModalWindow>
            )}
          </div>
        </div>
      )}
    </ModalWindow>
  );
};

export default MenuProfile;
