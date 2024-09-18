import { Dispatch, SetStateAction, useCallback, useEffect, useState } from 'react';
import cn from 'classnames';
import { useRouter } from 'next/navigation';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import { useTheme } from '@/src/hooks/useTheme';
import ModalWindow from '@/src/components/ModalWindow';
import { currentUserSelector } from '@/src/reduxjs/auth/selectors';
import { requestStatusSelector } from '@/src/reduxjs/base/selectors';
import ChangeThemeIcon from '@/src/ui/ChangeThemeIcon';
import ChangePasswordMenu from './components/ChangePasswordWindow';
import ChangeEmailWindow from './components/ChangeEmailWindow';
import s from './MenuSettings.module.scss';

interface Props {
  setIsSettingsOpen: Dispatch<SetStateAction<boolean>>;
}

const MenuSettings = ({ setIsSettingsOpen }: Props): React.ReactElement => {
  const [isLogoutConfirmOpen, setIsLogoutConfirmOpen] = useState(false);
  const [isDeleteAccountConfirmOpen, setIsDeleteAccountConfirmOpen] = useState(false);
  const [isChangePasswordOpen, setIsChangePasswordOpen] = useState(false);
  const [isChangeEmailOpen, setIsChangeEmailOpen] = useState(false);
  const currentUser = useAppSelector(currentUserSelector);
  const requestStatus = useAppSelector(requestStatusSelector);
  const [, toggleTheme] = useTheme();
  const { logout, deleteAccount } = useActions();
  const router = useRouter();

  const handleLogoutClick = () => setIsLogoutConfirmOpen(true);

  const handleConfirmLogoutClick = () => logout();

  const handleChangePasswordClick = () => setIsChangePasswordOpen(true);

  const handleChangeEmailClick = () => setIsChangeEmailOpen(true);

  const handleThemeChangeClick = () => toggleTheme();

  const handleDeleteAccount = () => setIsDeleteAccountConfirmOpen(true);

  const handleConfirmDeleteAccount = useCallback(() => {
    const isCurrentUserExist = currentUser?.id;
    if (isCurrentUserExist) {
      deleteAccount({ userId: currentUser?.id });
    }
  }, [currentUser?.id, deleteAccount]);

  useEffect(() => {
    const { isOk, method, request } = requestStatus;
    const isLogoutStatusCompleted = isOk && method === 'DELETE' && request === '/logout';
    const isDeleteAccountStatusCompleted = isOk && method === 'DELETE' && request === '/user';
    const isChangePasswordStatusCompleted = isOk && method === 'PATCH' && request === '/password';
    if (isLogoutStatusCompleted || isDeleteAccountStatusCompleted) {
      router.push('/auth/login');
    }
    if (isChangePasswordStatusCompleted) {
      setIsChangePasswordOpen(false);
    }
  }, [requestStatus, router]);

  return (
    <ModalWindow setIsActive={setIsSettingsOpen} title={'Settings'}>
      <div className={s.root}>
        <div className={s.section}>
          <button className={s.button} onClick={handleChangeEmailClick}>
            Change email <p className={s.parameter}>{currentUser?.email}</p>
          </button>
          {isChangeEmailOpen && (
            <ChangeEmailWindow
              currentUser={currentUser}
              setIsChangeEmailOpen={setIsChangeEmailOpen}
            />
          )}
          <button className={s.button} onClick={handleChangePasswordClick}>
            Change password
          </button>
          {isChangePasswordOpen && (
            <ChangePasswordMenu
              setIsChangePasswordOpen={setIsChangePasswordOpen}
              currentUser={currentUser}
            />
          )}
          <button className={s.button} onClick={handleThemeChangeClick}>
            Change Theme
            <ChangeThemeIcon size={30} />
          </button>
        </div>
        <div className={s.section}>
          <button className={cn(s.button, s.dangerButton)} onClick={handleLogoutClick}>
            Logout
          </button>
          {isLogoutConfirmOpen && (
            <ModalWindow
              title="Are you sure you want to logout?"
              setIsActive={setIsLogoutConfirmOpen}
              onConfirmClick={handleConfirmLogoutClick}
              buttonInfo={{ withConfirmButton: true }}
            />
          )}
          <button className={cn(s.button, s.dangerButton)} onClick={handleDeleteAccount}>
            Delete account
          </button>
          {isDeleteAccountConfirmOpen && (
            <ModalWindow
              title="Are you sure you want to delete your account?"
              setIsActive={setIsDeleteAccountConfirmOpen}
              onConfirmClick={handleConfirmDeleteAccount}
              buttonInfo={{ withConfirmButton: true }}
            />
          )}
        </div>
      </div>
    </ModalWindow>
  );
};

export default MenuSettings;
