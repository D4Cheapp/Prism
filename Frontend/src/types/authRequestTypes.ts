export type LoginRequestType = {
  email: string;
  password: string;
};
export type RegistrationRequestType = {
  email: string;
  password: string;
  confirmPassword: string;
  isDeveloper: boolean;
};
export type ChangePasswordType = {
  id: number;
  oldPassword: string;
  newPassword: string;
};
export type ChangeEmailType = {
  id: number;
  email: string;
};
