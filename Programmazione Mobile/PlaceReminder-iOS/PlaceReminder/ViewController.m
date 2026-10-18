//
//  ViewController.m
//  PlaceReminder
//
//  Created by Vincenzo Puca on 07/06/23.
//

#import <UserNotifications/UserNotifications.h>
#import <CoreLocation/CoreLocation.h>
#import "ViewController.h"
#import "Place.h"
#import "MapViewController.h"
#import "CustomAnnotation.h"
#import <UIKit/UIKit.h>

@interface ViewController ()

@end

@implementation ViewController

#pragma mark - ViewController Load

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.places = [[NSMutableArray alloc] init];
    
    self.geocoder = [[CLGeocoder alloc] init];
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    // Inizializza e configura il CLLocationManager
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    [self.locationManager requestWhenInUseAuthorization]; // Richiedi l'autorizzazione per ottenere la posizione corrente dell'utente
    [self.locationManager startUpdatingLocation]; // Avvia l'aggiornamento della posizione corrente dell'utente
    
    self.center = [UNUserNotificationCenter currentNotificationCenter];
    self.center.delegate=self;
    
    UNAuthorizationOptions options = UNAuthorizationOptionAlert + UNAuthorizationOptionSound;
    
    [self.center requestAuthorizationWithOptions:options
     completionHandler:^(BOOL granted, NSError * _Nullable error) {
      if (!granted) {
        NSLog(@"Something went wrong");
      }
    }];
    
    [self.center getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
      if (settings.authorizationStatus != UNAuthorizationStatusAuthorized) {
        // Notifications not allowed
      }
    }];
    
    [self loadPlacesFromFile];
    
    [self.tableView reloadData];
}

- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions options))completionHandler {
   
   completionHandler(UNNotificationPresentationOptionBanner);
   
}

#pragma mark - Buttons cell pressed

- (IBAction)AddNewPlaceButton:(id)sender {
    // Mostra il dialog per l'inserimento di un nuovo luogo
    [self showAddDialog];
}

- (void)showAddDialog {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Insert new Place" message:nil preferredStyle:UIAlertControllerStyleAlert];
    
    [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = @"Name";
    }];
    
    [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = @"Location address";
    }];
    
    [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = @"Description";
    }];
    
    [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = @"Latitude";
        textField.keyboardType = UIKeyboardTypeDecimalPad;
    }];
    
    [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = @"Longitude";
        textField.keyboardType = UIKeyboardTypeDecimalPad;
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"Add" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        UITextField *customNameField = alertController.textFields.firstObject;
        UITextField *nameTextField = alertController.textFields[1];
        UITextField *descriptionTextField = alertController.textFields[2];
        UITextField *latitudeTextField = alertController.textFields[3];
        UITextField *longitudeTextField = alertController.textFields[4];
        
        NSString *customName = customNameField.text;
        NSString *name = nameTextField.text;
        NSString *descriptionText = descriptionTextField.text;
        double latitude = [latitudeTextField.text doubleValue];
        double longitude = [longitudeTextField.text doubleValue];
        
        if(customNameField.hasText == YES){
            if (latitudeTextField.text.length!=0 && longitudeTextField.text.length!=0) {
                CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(latitude, longitude);
                [self performReverseGeocodingWithCoordinate:coordinate completionHandler:^(CLPlacemark *placemark, NSError *error) {
                    if (error) {
                        [self showReverseGeocodingError];
                    } else {
                        [self addPlaceWithPlacemark:placemark descriptionText:descriptionText customName:customName];
                    }
                }];
            } else {
                [self performReverseGeocodingWithAddress:name completionHandler:^(CLPlacemark *placemark, NSError *error) {
                    if (error) {
                        [self showReverseGeocodingError];
                    } else {
                        [self addPlaceWithPlacemark:placemark descriptionText:descriptionText customName:customName];
                    }
                }];
            }
            
        }else{
            [self showReverseGeocodingError];
        }
    }];
    
    [alertController addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

- (IBAction)DeletePlaceButton:(id)sender {
    // Pulsante "Delete" premuto
    UIButton *deleteButton = (UIButton *)sender;
    
    // Cella corrispondente
    UITableViewCell *cell = (UITableViewCell *)deleteButton.superview.superview;
    
    // Salva l'indice della cella nella tabella
    NSIndexPath *indexPath = [self.tableView indexPathForCell:cell];
    
    if (indexPath) {
        // Rimuovi il Place dalla lista dei luoghi
        [self.places removeObjectAtIndex:indexPath.row];
        
        // Ricarica la tabella per visualizzare l'aggiornamento
        [self.tableView reloadData];
        
        [self savePlacesToFile];
    }
}
- (IBAction)EditPlaceButton:(id)sender {
    // Pulsante "Edit" premuto
    UIButton *editButton = (UIButton *)sender;
    
    // Cella corrispondente
    UITableViewCell *cell = (UITableViewCell *)editButton.superview.superview;
    
    // Salva l'indice della cella nella tabella
    NSIndexPath *indexPath = [self.tableView indexPathForCell:cell];
    
    if (indexPath) {
        // Place corrispondente per l'indice selezionato
        Place *place = self.places[indexPath.row];
        
        // Mostra un alert controller per la modifica dei valori del place
        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Edit Place" message:nil preferredStyle:UIAlertControllerStyleAlert];
        
        [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
            textField.placeholder = @"Name";
            textField.text=place.customName;
        }];
        
        [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
            textField.placeholder = @"Location name";
            textField.text = place.name;
        }];
        
        [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
            textField.placeholder = @"Description";
            textField.text = place.descriptionText;
        }];
        
        [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
            textField.placeholder = @"Latitude";
            textField.keyboardType = UIKeyboardTypeDecimalPad;
            textField.text = [NSString stringWithFormat:@"%f", place.latitude];
        }];
        
        [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
            textField.placeholder = @"Longitude";
            textField.keyboardType = UIKeyboardTypeDecimalPad;
            textField.text = [NSString stringWithFormat:@"%f", place.longitude];
        }];
        
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
        
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"Save" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            UITextField *customNameField = alertController.textFields.firstObject;
            UITextField *nameTextField = alertController.textFields[1];
            UITextField *descriptionTextField = alertController.textFields[2];
            UITextField *latitudeTextField = alertController.textFields[3];
            UITextField *longitudeTextField = alertController.textFields[4];
            
            // Aggiorna i valori del Place con i nuovi dati inseriti
            place.customName=customNameField.text;
            place.name = nameTextField.text;
            place.descriptionText = descriptionTextField.text;
            place.latitude = [latitudeTextField.text doubleValue];
            place.longitude = [longitudeTextField.text doubleValue];
            NSDate *currentDate = [NSDate date];
            place.dateAdded = currentDate;
            
            if(customNameField.hasText == YES){
                if (latitudeTextField.text.length != 0 && longitudeTextField.text.length != 0) {
                    CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(place.latitude, place.longitude);
                    [self performReverseGeocodingWithCoordinate:coordinate completionHandler:^(CLPlacemark *placemark, NSError *error) {
                        if (error) {
                            [self showReverseGeocodingError];
                        } else {
                            place.name = [self formattedAddressFromPlacemark:placemark];
                            [self.places replaceObjectAtIndex:indexPath.row withObject:place];
                            [self.places removeObjectAtIndex:indexPath.row];
                            [self.places insertObject:place atIndex:0];
                            
                            [self savePlacesToFile];
                            
                            [self.tableView reloadData];
                        }
                    }];
                } else {
                    [self performReverseGeocodingWithAddress:nameTextField.text completionHandler:^(CLPlacemark *placemark, NSError *error) {
                        if (error) {
                            [self showReverseGeocodingError];
                        } else {
                            place.name = [self formattedAddressFromPlacemark:placemark];
                            place.latitude = placemark.location.coordinate.latitude;
                            place.longitude = placemark.location.coordinate.longitude;
                            [self.places replaceObjectAtIndex:indexPath.row withObject:place];
                            [self.places removeObjectAtIndex:indexPath.row];
                            [self.places insertObject:place atIndex:0];
                            
                            [self savePlacesToFile];
                            
                            [self.tableView reloadData];
                        }
                    }];
            }
            }else{
                [self showReverseGeocodingError];
            }
        }];
        
        [alertController addAction:cancelAction];
        [alertController addAction:okAction];
        
        [self presentViewController:alertController animated:YES completion:nil];
    }
}

#pragma mark - Methods tableView

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.places.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell" forIndexPath:indexPath];
    
    // Recupera il Place corrispondente all'indice
    Place *place = self.places[indexPath.row];
    
    // Recupera le label dalla cella utilizzando i tag
    UILabel *nameCustomLabel = [cell viewWithTag:10];
    UILabel *nameLabel = [cell viewWithTag:1];
    UILabel *descriptionLabel = [cell viewWithTag:2];
    UILabel *coordinatesLabel = [cell viewWithTag:3];
    UILabel *dateTimeLabel = [cell viewWithTag:4];
    
    // Imposta i testi delle label con le informazioni del Place
    nameLabel.text = place.name;
    descriptionLabel.text = place.descriptionText;
    coordinatesLabel.text = [NSString stringWithFormat:@"Lat: %f, Long: %f", place.latitude, place.longitude];
    nameCustomLabel.text=place.customName;
    
    // Formatta la data
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"dd/MM/yyyy HH:mm:ss";
    NSString *formattedDate = [dateFormatter stringFromDate:place.dateAdded];
    dateTimeLabel.text = formattedDate;
    
    return cell;
}

#pragma mark - Map Methods

- (IBAction)mapPressed:(id)sender {

    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    MapViewController *mapViewController = [storyboard instantiateViewControllerWithIdentifier:@"MapView"];
    mapViewController.places = self.places;
    [self performSegueWithIdentifier:@"SegueSendPlaces" sender:nil];
    [self.navigationController pushViewController:mapViewController animated:YES];

}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"SegueSendPlaces"]) {
        MapViewController *mapViewController = (MapViewController *)segue.destinationViewController;
        mapViewController.places = self.places;
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    // Nascondi il navigationItem
    [self.navigationController setNavigationBarHidden:YES animated:animated];
}


#pragma mark - Location Notification

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations {
    CLLocation *userLocation = [locations lastObject];
    // Raggio di distanza per definire quando l'utente è nelle prossimità di un place 100 metri
    CLLocationDistance radius = 100;
    
    for (Place *place in self.places) {
        // Calcola la distanza tra la posizione corrente dell'utente e ciascun place nell'array
        CLLocation *placeLocation = [[CLLocation alloc] initWithLatitude:place.latitude longitude:place.longitude];
        CLLocationDistance distance = [userLocation distanceFromLocation:placeLocation];
        
        if (distance <= radius) {
            
            UNMutableNotificationContent *content = [UNMutableNotificationContent new];
            content.title = @"You are close to a place!";
            content.body = place.customName;
            content.sound = [UNNotificationSound defaultSound];
            
            UNTimeIntervalNotificationTrigger *trigger = [UNTimeIntervalNotificationTrigger
              triggerWithTimeInterval:5 repeats:NO];
            
            NSString *identifier =place.name; // Do not show notification if two addresses are equals
            UNNotificationRequest *request = [UNNotificationRequest requestWithIdentifier:identifier
                                                                                  content:content trigger:trigger];

            [self.center addNotificationRequest:request withCompletionHandler:^(NSError * _Nullable error) {
              if (error != nil) {
                NSLog(@"Something went wrong on notification: %@",error);
              }else{NSLog(@"Notification Added");}
            }];
        }//if
    }
}

#pragma mark - Reverse Geocoding Methods

- (void)performReverseGeocodingWithCoordinate:(CLLocationCoordinate2D)coordinate completionHandler:(void (^)(CLPlacemark *, NSError *))completionHandler {
    CLLocation *location = [[CLLocation alloc] initWithLatitude:coordinate.latitude longitude:coordinate.longitude];
    
    [self.geocoder reverseGeocodeLocation:location completionHandler:^(NSArray<CLPlacemark *> *placemarks, NSError *error) {
        if (error) {
            completionHandler(nil, error);
        } else if (placemarks.count > 0) {
            CLPlacemark *placemark = [placemarks firstObject];
            completionHandler(placemark, nil);
        } else {
            NSError *noPlacemarksError = [NSError errorWithDomain:@"it.vincenzopuca.ReverseGeocoding" code:0 userInfo:@{NSLocalizedDescriptionKey: @"No placemarks found."}];
            completionHandler(nil, noPlacemarksError);
        }
    }];
}

- (void)performReverseGeocodingWithAddress:(NSString *)address completionHandler:(void (^)(CLPlacemark *, NSError *))completionHandler {
    [self.geocoder geocodeAddressString:address completionHandler:^(NSArray<CLPlacemark *> *placemarks, NSError *error) {
        if (error) {
            completionHandler(nil, error);
        } else if (placemarks.count > 0) {
            CLPlacemark *placemark = [placemarks firstObject];
            completionHandler(placemark, nil);
        } else {
            NSError *noPlacemarksError = [NSError errorWithDomain:@"it.vincenzopuca.ReverseGeocoding" code:0 userInfo:@{NSLocalizedDescriptionKey: @"No placemarks found."}];
            completionHandler(nil, noPlacemarksError);
        }
    }];
}

- (void)showReverseGeocodingError {
    UIAlertController *errorDialog = [UIAlertController alertControllerWithTitle:@"Errore" message:@"Error during Reverse Geocoding or Getting fields information, retry." preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [errorDialog addAction:okAction];
    
    [self presentViewController:errorDialog animated:YES completion:nil];
}

#pragma mark - Helper Methods

- (NSString *)formattedAddressFromPlacemark:(CLPlacemark *)placemark {
    NSMutableString *address = [NSMutableString string];
    
    if (placemark.thoroughfare) {
        [address appendString:placemark.thoroughfare];
    }
    
    if (placemark.subThoroughfare) {
        if (address.length > 0) {
            [address appendString:@", "];
        }
        [address appendString:placemark.subThoroughfare];
    }
    
    if (placemark.locality) {
        if (address.length > 0) {
            [address appendString:@"\n"];
        }
        [address appendString:placemark.locality];
    }
    
    if (placemark.administrativeArea) {
        if (address.length > 0) {
            [address appendString:@", "];
        }
        [address appendString:placemark.administrativeArea];
    }
    
    if (placemark.country) {
        if (address.length > 0) {
            [address appendString:@", "];
        }
        [address appendString:placemark.country];
    }
    
    return address;
}

- (void)addPlaceWithPlacemark:(CLPlacemark *)placemark descriptionText:(NSString *)descriptionText customName:(NSString *)customName{
    Place *place = [[Place alloc] init];
    place.customName=customName;
    place.name = [self formattedAddressFromPlacemark:placemark];
    place.latitude = placemark.location.coordinate.latitude;
    place.longitude = placemark.location.coordinate.longitude;
    place.descriptionText = descriptionText;
    NSDate *currentDate = [NSDate date];
    place.dateAdded = currentDate;
    
    // Aggiunta di place alla lista dei luoghi
    NSInteger index = 0;
    for (Place *existingPlace in self.places) {
        if ([place.dateAdded compare:existingPlace.dateAdded] == NSOrderedDescending) {
            break;
        }
        index++;
    }
    
    [self.places insertObject:place atIndex:index];
    
    [self.tableView reloadData];
    
    [self savePlacesToFile];
    
    [self printPlaces]; // Debug list
}

#pragma mark - Debug Methods

- (void)printPlaces { // Debug Added Places
    for (Place *place in self.places) {
        NSLog(@"Name: %@Address: %@, Latitude: %f, Longitude: %f, Description: %@, Date Added: %@",place.customName, place.name, place.latitude, place.longitude, place.descriptionText, place.dateAdded);
    }
}

#pragma mark - Methods Data Store

- (void)savePlacesToFile {
    
    NSString *fileName = @"places.dat";
    NSString *filePath = [self getFilePathWithName:fileName];

    NSFileManager *fileManager = [NSFileManager defaultManager];

    // Verifica se il file esiste già
    if (![fileManager fileExistsAtPath:filePath]) {
        // Crea il nuovo file
        BOOL success = [fileManager createFileAtPath:filePath contents:nil attributes:nil];
        if (success) {
            NSLog(@"File di salvataggio creato con successo.");
        } else {
            NSLog(@"Errore nella creazione del file di salvataggio.");
        }
        
    } else {
        NSLog(@"Il file di salvataggio esiste già.");
    }

    BOOL success = [NSKeyedArchiver archiveRootObject:self.places toFile:filePath];
    if (success) {
        NSLog(@"Dati salvati con successo nel file: %@", filePath);
    } else {
        NSLog(@"Errore nel salvataggio dei dati nel file.");
    }
}

- (void)loadPlacesFromFile {
    NSString *filePath = [self getFilePathWithName:@"places.dat"];
    NSMutableArray *places = [NSKeyedUnarchiver unarchiveObjectWithFile:filePath];
    if (places) {
        self.places = places;
        NSLog(@"Array places caricato con successo.");
    } else {
        NSLog(@"Errore nel caricamento dell'array places.");
    }
}

- (NSString *)getFilePathWithName:(NSString *)fileName {
    NSArray *documentDirectories = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentDirectory = [documentDirectories objectAtIndex:0];
    NSString *filePath = [documentDirectory stringByAppendingPathComponent:fileName];
    return filePath;
}

@end
