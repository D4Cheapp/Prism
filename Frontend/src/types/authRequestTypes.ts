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
