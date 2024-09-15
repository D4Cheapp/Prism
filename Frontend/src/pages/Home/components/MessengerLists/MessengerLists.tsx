import { Dispatch, SetStateAction } from 'react';
import cn from 'classnames';
import s from './MessengerLists.module.scss';

interface Props {
  isDialogOpen: boolean;
  setIsOpenDialog: Dispatch<SetStateAction<boolean>>;
}

const MessengerLists = ({ isDialogOpen, setIsOpenDialog }: Props): React.ReactElement => {
  const handleDialogToggleClick = () => setIsOpenDialog((state) => !state);
  return (
    <section className={cn(s.lists, { [s.sideLists]: isDialogOpen })}>
      <div className={s.header}>Header</div>
      Messenger lists works!
      <button onClick={handleDialogToggleClick}>open dialog</button>
    </section>
  );
};

export default MessengerLists;
