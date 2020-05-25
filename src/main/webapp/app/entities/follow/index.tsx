import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Follow from './follow';
import FollowDetail from './follow-detail';
import FollowUpdate from './follow-update';
import FollowDeleteDialog from './follow-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FollowDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FollowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FollowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FollowDetail} />
      <ErrorBoundaryRoute path={match.url} component={Follow} />
    </Switch>
  </>
);

export default Routes;
