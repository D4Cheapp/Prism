import { bindActionCreators } from 'redux';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import { AppDispatchType, RootStateType, reducersActions } from '../reduxjs';

export const useActions = () => {
  const dispatch = useDispatch<AppDispatchType>();
  return bindActionCreators(reducersActions, dispatch);
};
export const useAppSelector: TypedUseSelectorHook<RootStateType> = useSelector;
