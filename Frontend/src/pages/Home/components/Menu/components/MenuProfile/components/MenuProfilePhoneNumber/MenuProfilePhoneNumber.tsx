import React, { ChangeEvent, useEffect, useState } from 'react';
import { useActions } from '@/src/hooks/reduxHooks';
import ModalWindow from '@/src/components/ModalWindow';
import CustomInput from '@/src/ui/CustomInput';
import { CurrentUserProfileType } from '@/src/types/profileReceiveTypes';
import s from './MenuProfilePhoneNumber.module.scss';

interface Props {
  profile: CurrentUserProfileType;
  setChangedProfileInfo: (currentVal: {
    property: keyof NonNullable<CurrentUserProfileType>;
    value: string | number;
  }) => void;
  setMessagesState: ({ error, success }: { error?: string; success?: string }) => void;
}

const MenuProfilePhoneNumber = ({
  profile,
  setChangedProfileInfo,
  setMessagesState,
}: Props): React.ReactElement => {
  const [isChangePhoneNumberWindowOpen, setIsChangePhoneNumberWindowOpen] =
    useState<boolean>(false);
  const [phoneNumberInput, setPhoneNumberInput] = useState<string>();
  const { setProfilePhoneNumber } = useActions();

  const handlePhoneNumberClick = () => setIsChangePhoneNumberWindowOpen(true);

  const handlePhoneNumberChangeConfirm = () => {
    const isPhoneNumberFormatCorrect =
      /^\+?\d{1}[-.\s]?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{2}[-.\s]?\d{2}/.test(
        phoneNumberInput ?? '',
      );
    const isPhoneNumberValid = phoneNumberInput && isPhoneNumberFormatCorrect;
    if (isPhoneNumberValid) {
      setProfilePhoneNumber({ property: phoneNumberInput });
      setChangedProfileInfo({ property: 'phoneNumber', value: phoneNumberInput });
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

  const handlePhoneNumberChange = (e: ChangeEvent<HTMLInputElement>) => {
    setPhoneNumberInput(e.target.value);
  };

  useEffect(() => {
    const isProfileFetched = profile !== null;
    if (isProfileFetched) {
      setPhoneNumberInput(profile?.phoneNumber ?? '');
    }
  }, [profile]);

  return (
    <div className={s.attribute}>
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
            autoFocus
            placeholder="+ 7 ( _ _ _ ) _ _ _ - _ _ - _ _"
            mask="+9 (999) 999-99-99"
            onChange={handlePhoneNumberChange}
            value={phoneNumberInput}
            classNames={{ input: s.modalInput }}
          />
        </ModalWindow>
      )}
    </div>
  );
};

export default MenuProfilePhoneNumber;
