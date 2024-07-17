import * as Yup from 'yup';

export const passwordValidationSchema = Yup.string()
  .min(6, 'Password must be at least 6 characters')
  .max(25, 'Password must be less than 25 characters')
  .matches(/.*[A-ZА-Я].*/, 'Password must contain at least one uppercase letter')
  .matches(/.*[a-zа-я].*/, 'Password must contain at least one lowercase letter')
  .matches(/.*[0-9].*/, 'Password must contain at least one number')
  .matches(
    /.*[!\";#$%&'()*+,-./:<=>?@^_`{|}~].*/,
    'Password must contain at least one special character',
  );

export const confirmCodeValidation = Yup.string()
  .min(4, 'Invalid code')
  .max(4, 'Invalid code')
  .required('Code is required');

export const confirmPasswordValidationSchema = Yup.string()
  .required()
  .oneOf([Yup.ref('password')], 'Passwords must match');
