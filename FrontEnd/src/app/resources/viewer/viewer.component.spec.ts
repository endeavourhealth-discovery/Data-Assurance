import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewerComponent } from './viewer.component';
import {ObjectViewerComponent} from '../object-viewer/object-viewer.component';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {MockNgbActiveModal} from '../../mocks/mock.ngb-active-modal';
import {ServicePatientResource} from '../../models/Resource';

describe('ViewerComponent', () => {
  let component: ViewerComponent;
  let fixture: ComponentFixture<ViewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [NgbModule.forRoot()],
      declarations: [ ViewerComponent, ObjectViewerComponent ],
      providers: [
        {provide : NgbActiveModal, useClass: MockNgbActiveModal }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewerComponent);
    component = fixture.componentInstance;
    component.resource = { resourceJson: {} } as ServicePatientResource;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
