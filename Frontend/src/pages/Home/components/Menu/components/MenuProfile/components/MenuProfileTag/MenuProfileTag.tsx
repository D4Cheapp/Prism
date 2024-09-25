import React, { useRef, useState } from 'react';
import { useActions } from '@/src/hooks/reduxHooks';
import ModalWindow from '@/src/components/ModalWindow';
import CustomInput from '@/src/ui/CustomInput';
import { CurrentUserProfileType } from '@/src/types/profileReceiveTypes';
import s from './MenuProfileTag.module.scss';

interface Props {
  profile: CurrentUserProfileType;
  setChangedProfileInfo: (changedProfileInfo: {
    property: keyof NonNullable<CurrentUserProfileType>;
    value: string | number;
  }) => void;
  setMessagesState: ({ error, success }: { error?: string; success?: string }) => void;
}

const MenuProfileTag = ({
  profile,
  setChangedProfileInfo,
  setMessagesState,
}: Props): React.ReactElement => {
  const [isChangeTagWindowOpen, setIsChangeTagWindowOpen] = useState<boolean>(false);
  const tagInputRef = useRef<HTMLInputElement>(null);
  const { setProfileTag } = useActions();

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

  return (
    <div className={s.attribute}>
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
    </div>
  );
};

export default MenuProfileTag;
