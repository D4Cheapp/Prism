'use client';
import * as Yup from 'yup';
import { useActions } from '@/src/hooks/reduxHooks';
import Auth from '@/src/pages/Auth/Auth';
import { AuthFormType } from '@/src/types/formTypes';

const LoginPage = () => {
  const { setErrorsState, login } = useActions();

  const handleLoginValidate = Yup.object({
    email: Yup.string().required('Email is required'),
    password: Yup.string().required('Password is required'),
  });

  const handleLoginClick = async (values: AuthFormType) => {
    let isError = false;
    await handleLoginValidate.validate(values).catch((error: Yup.ValidationError) => {
      isError = true;
      setErrorsState(error.errors[0]);
    });
    if (!isError) {
      login(values);
    }
  };

  return (
    <Auth
      title="Login"
      redirectTo="/registration"
      redirectText="Don't have an account?"
      onFormSubmitClick={handleLoginClick}
    />
  );
};

export default LoginPage;
