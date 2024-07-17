import React from 'react';
import { Metadata } from 'next';
import './styles/_variables.scss';
import './styles/globals.scss';
import MessageContainer from '../components/MessageContainer';
import Providers from '../components/Providers';

export const metadata: Metadata = {
  title: 'Prism',
};

interface Props {
  children: React.ReactNode;
}

const RootLayout = ({ children }: Props): React.ReactNode => {
  return (
    <html lang="en">
      <Providers>
        <div className="noise" />
        <MessageContainer />
        {children}
      </Providers>
    </html>
  );
};

export default RootLayout;
