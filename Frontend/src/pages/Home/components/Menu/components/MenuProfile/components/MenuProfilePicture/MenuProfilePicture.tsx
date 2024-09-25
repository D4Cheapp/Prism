import React, { ChangeEvent, useRef, useState } from 'react';
import Image from 'next/image';
import { useActions } from '@/src/hooks/reduxHooks';
import ModalWindow from '@/src/components/ModalWindow';
import Profile from 'public/icons/menu/profile.svg';
import Bin from 'public/icons/common/bin.svg';
import { CurrentUserProfileType } from '@/src/types/profileReceiveTypes';

import s from './MenuProfilePicture.module.scss';

interface Props {
  profile: CurrentUserProfileType;
  setChangedProfileInfo: (currentVal: {
    property: keyof NonNullable<CurrentUserProfileType>;
    value: string | number;
  }) => void;
}

const MenuProfilePicture = ({ profile, setChangedProfileInfo }: Props): React.ReactElement => {
  const { setProfilePicture, deleteProfilePicture } = useActions();
  const [isConfirmProfilePictureDeleteWindowOpen, setIsConfirmProfilePictureDeleteWindowOpen] =
    useState<boolean>(false);
  const profilePictureInputRef = useRef<HTMLInputElement>(null);

  const handleProfilePictureClick = () => {
    profilePictureInputRef.current?.click();
  };

  const handleProfilePictureChange = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const fileReader = new FileReader();
      fileReader.onload = () => {
        setChangedProfileInfo({ property: 'profilePicture', value: fileReader.result as string });
        setProfilePicture(fileReader.result as string);
      };
      fileReader.readAsDataURL(file);
    }
  };

  const handleDeleteProfilePictureClick = () => {
    setIsConfirmProfilePictureDeleteWindowOpen(true);
  };

  const handleConfirmProfilePictureDeleteClick = () => {
    deleteProfilePicture();
    setChangedProfileInfo({ property: 'profilePicture', value: '' });
    setIsConfirmProfilePictureDeleteWindowOpen(false);
  };

  return (
    <>
      <div className={s.profilePictureContainer}>
        <div className={s.iconOverlay}>
          {profile?.profilePicture ? (
            <>
              <Image
                className={s.icon}
                src={
                  profile.profilePicture.includes('data:image')
                    ? profile.profilePicture
                    : `data:image/png;base64,${profile.profilePicture}`
                }
                alt="profile"
                width={70}
                height={70}
                onClick={handleProfilePictureClick}
              />
              <Bin onClick={handleDeleteProfilePictureClick} className={s.deleteIcon} />
            </>
          ) : (
            <Profile className={s.icon} onClick={handleProfilePictureClick} />
          )}
        </div>
      </div>
      {isConfirmProfilePictureDeleteWindowOpen && (
        <ModalWindow
          setIsActive={setIsConfirmProfilePictureDeleteWindowOpen}
          title="Are you sure?"
          onConfirmClick={handleConfirmProfilePictureDeleteClick}
          buttonInfo={{
            confirmTitle: 'Yes',
            withConfirmButton: true,
            secondButtonTitle: 'No',
          }}
        />
      )}
      <input
        type="file"
        accept="image/*"
        className={s.hiddenInput}
        ref={profilePictureInputRef}
        onChange={(e) => void handleProfilePictureChange(e)}
      />
    </>
  );
};

export default MenuProfilePicture;
