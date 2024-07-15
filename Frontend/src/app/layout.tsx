import React from 'react';
import { Metadata } from 'next';
import './styles/_variables.scss';
import './styles/globals.scss';
import ErrorContainer from '../components/ErrorContainer';
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
        <ErrorContainer />
        {children}
      </Providers>
    </html>
  );
};

export default RootLayout;
