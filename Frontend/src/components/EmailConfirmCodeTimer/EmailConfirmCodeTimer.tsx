import React, { useEffect, useState } from 'react';
import s from './EmailConfirmCodeTimer.module.scss';

interface Props {
  onResendButtonClick?: () => Promise<void> | void;
}

const EmailConfirmCodeTimer = ({ onResendButtonClick }: Props): React.ReactElement => {
  const [sendAgainBlockTimer, setSendAgainBlockTimer] = useState<number>(60);
  const timerSeconds = sendAgainBlockTimer % 60;
  const timerMinutes = Math.floor(sendAgainBlockTimer / 60);

  const handleSendEmailAgainClick = async () => {
    if (onResendButtonClick) {
      setSendAgainBlockTimer(60);
      await onResendButtonClick();
    }
  };

  useEffect(() => {
    const timer = setInterval(() => {
      if (sendAgainBlockTimer > 0) {
        setSendAgainBlockTimer((time) => time - 1);
      } else {
        () => clearInterval(timer);
      }
    }, 1000);
    return () => clearInterval(timer);
  }, [sendAgainBlockTimer]);

  return (
    <>
      {sendAgainBlockTimer > 0 ? (
        <p className={s.resendText}>
          Please, wait {timerMinutes}:{timerSeconds < 10 ? `0${timerSeconds}` : timerSeconds} to
          resend confirm code
        </p>
      ) : (
        <button
          className={s.resendButton}
          type="button"
          onClick={() => void handleSendEmailAgainClick()}
        >
          <p className={s.resendButtonText}>Send confirm code again</p>
        </button>
      )}
    </>
  );
};

export default EmailConfirmCodeTimer;
