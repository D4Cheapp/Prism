import cn from 'classnames';
import s from './MessengerLists.module.scss';

interface Props {
  isDialogOpen: boolean;
}

const MessengerLists = ({ isDialogOpen }: Props): React.ReactElement => {
  return (
    <section className={cn(s.lists, { [s.sideLists]: isDialogOpen })}>
      Messenger lists works!
    </section>
  );
};

export default MessengerLists;
