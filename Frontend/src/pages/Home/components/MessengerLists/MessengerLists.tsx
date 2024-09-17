import { Dispatch, SetStateAction } from 'react';
import { SelectedCategoryType } from '@/src/types/messengerTypes';
import s from './MessengerLists.module.scss';

interface Props {
  selectedCategory: SelectedCategoryType;
  setIsOpenDialog: Dispatch<SetStateAction<boolean>>;
}

const MessengerLists = ({ setIsOpenDialog, selectedCategory }: Props): React.ReactElement => {
  const handleDialogToggleClick = () => setIsOpenDialog((state) => !state);
  return (
    <section className={s.lists}>
      <div className={s.header}>Header</div>
      {selectedCategory} lists works!
      <button onClick={handleDialogToggleClick}>open dialog</button>
    </section>
  );
};

export default MessengerLists;
