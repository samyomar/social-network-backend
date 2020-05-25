import { Moment } from 'moment';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IProfilePhotos {
  id?: number;
  imageContentType?: string;
  image?: any;
  uploadedDate?: string;
  title?: string;
  userProfile?: IUserProfile;
}

export const defaultValue: Readonly<IProfilePhotos> = {};
