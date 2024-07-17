export type AuthFormType = {
  email: string;
  password: string;
  confirmPassword: string;
  isDeveloper: boolean;
};
export type ConfirmCodeFormType = {
  code: string;
  password?: string;
  confirmPassword?: string;
};
