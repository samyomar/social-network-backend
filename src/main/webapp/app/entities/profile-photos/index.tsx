import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProfilePhotos from './profile-photos';
import ProfilePhotosDetail from './profile-photos-detail';
import ProfilePhotosUpdate from './profile-photos-update';
import ProfilePhotosDeleteDialog from './profile-photos-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProfilePhotosDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProfilePhotosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProfilePhotosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProfilePhotosDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProfilePhotos} />
    </Switch>
  </>
);

export default Routes;
