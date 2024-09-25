'use client';
import React, { useCallback } from 'react';
import { useActions, useAppSelector } from '@/src/hooks/reduxHooks';
import { messageSelector } from '@/src/reduxjs/base/selectors';
import Message from './Message';
import s from './MessageContainer.module.scss';

const MessageContainer = (): React.ReactNode => {
  const messageState = useAppSelector(messageSelector);
  const isMessagesEmpty = messageState.length === 0;
  const { deleteMessageState } = useActions();

  const handleCloseClick = useCallback((index: number) => {
    deleteMessageState(index);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (isMessagesEmpty) {
    return null;
  }

  return (
    <div className={s.container}>
      {messageState.map((message) => (
        <Message
          key={message.id}
          message={message}
          index={message.id}
          onCloseClick={handleCloseClick}
        />
      ))}
    </div>
  );
};

export default MessageContainer;
